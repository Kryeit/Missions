package com.kryeit;

import java.text.ParseException;
import java.util.*;
import java.util.function.BiFunction;

public class JSONObject {
    private final Map<String, Object> data;
    public static final Null NULL = new Null();

    public JSONObject(String input) {
        data = new Parser(input.toCharArray()).parseJSONObject().data;
    }

    public JSONObject(Map<String, Object> data) {
        this.data = data;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) data.get(key);
    }

    public String getString(String key) {
        return (String) data.get(key);
    }

    public long getLong(String key) {
        return (long) data.get(key);
    }

    public JSONArray getArray(String key) {
        return (JSONArray) data.get(key);
    }

    public JSONObject getObject(String key) {
        return (JSONObject) data.get(key);
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return data.entrySet();
    }

    public Set<String> keySet() {
        return data.keySet();
    }

    public float getFloat(String key) {
        return (float) data.get(key);
    }

    public Optional<Float> optFloat(String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return Optional.of(((Number) value).floatValue());
        }
        return Optional.empty();
    }

    public static class JSONArray implements Iterable<Object> {
        private final List<Object> data;

        public JSONArray(List<Object> data) {
            this.data = data;
        }

        public JSONArray(String input) {
            Parser parser = new Parser(input.toCharArray());
            data = parser.parseJSONArray().data;
        }

        public boolean getBoolean(int i) {
            return (boolean) data.get(i);
        }

        public String getString(int i) {
            return (String) data.get(i);
        }

        public long getLong(int i) {
            return (long) data.get(i);
        }

        public JSONArray getArray(int i) {
            return (JSONArray) data.get(i);
        }

        public JSONObject getObject(int i) {
            return (JSONObject) data.get(i);
        }

        @Override
        public Iterator<Object> iterator() {
            return data.iterator();
        }

        public int size() {
            return data.size();
        }

        public <T> List<T> asList(BiFunction<JSONArray, Integer, T> mapper) {
            List<T> out = new ArrayList<>(size());
            for (int i = 0; i < size(); i++) {
                out.add(mapper.apply(this, i));
            }
            return out;
        }
    }

    private static class Parser {
        private final char[] data;
        private int pos = 0;

        private Parser(char[] data) {
            this.data = data;
        }

        private char currentChar() {
            return data[pos];
        }

        private void incrementPosition() {
            pos++;
        }

        private void skipWhiteSpaces() {
            while (Character.isWhitespace(currentChar())) {
                incrementPosition();
            }
        }

        private String parseString() {
            if (currentChar() == '\'') throw exception("JSON standard does not allow single quoted strings");
            if (currentChar() != '\"') throw exception("JSON standard allows only one top-level value");

            StringBuilder string = new StringBuilder();
            boolean escapeNextChar = false;

            while (true) {
                incrementPosition();
                if (escapeNextChar) {
                    escapeNextChar = false;
                    if (currentChar() == '\\') string.append('\\');
                    else if (currentChar() == '"') string.append('"');
                    else throw exception("Invalid escape sequence \\" + currentChar());
                    continue;
                } else if (currentChar() == '"') {
                    return string.toString();
                }

                if (currentChar() == '\\') {
                    escapeNextChar = true;
                    continue;
                }
                string.append(currentChar());
            }
        }

        private Object parseValue() {
            if (currentChar() == '"') {
                return parseString();
            } else if (Character.isDigit(currentChar())) {
                return parseNumber();
            } else if (currentChar() == 'f' || currentChar() == 't' || currentChar() == 'n') {
                return parseConstant();
            } else if (currentChar() == '{') {
                return parseJSONObject();
            } else if (currentChar() == '[') {
                return parseJSONArray();
            } else {
                throw exception("JSON standard does not allow such tokens");
            }
        }

        private Object parseConstant() {
            StringBuilder word = new StringBuilder();
            while (Character.isLetter(currentChar())) {
                word.append(currentChar());
                incrementPosition();
            }
            pos--;

            String wordString = word.toString();
            return switch (wordString) {
                case "true" -> true;
                case "false" -> false;
                case "null" -> NULL;
                default -> throw exception("JSON standard does not allow such tokens");
            };
        }

        private Number parseNumber() {
            StringBuilder number = new StringBuilder();
            while (currentChar() == '.' || Character.isDigit(currentChar())) {
                number.append(currentChar());
                incrementPosition();
            }
            pos--;
            return Double.parseDouble(number.toString());
        }

        private Map<String, Object> parseKeyValue() {
            String key = parseString();
            incrementPosition();
            skipWhiteSpaces();
            if (currentChar() != ':') throw exception("Expected '" + ':' + "', got '" + currentChar() + '\'');
            incrementPosition();
            skipWhiteSpaces();
            return Map.of(key, parseValue());
        }

        private JSONObject parseJSONObject() {
            incrementPosition();
            skipWhiteSpaces();
            Map<String, Object> out = new HashMap<>(parseKeyValue());
            incrementPosition();
            skipWhiteSpaces();
            while (currentChar() != '}') {
                if (currentChar() == ',') {
                    incrementPosition();
                    skipWhiteSpaces();
                    out.putAll(parseKeyValue());
                } else throw exception("',' or '}' expected");
                incrementPosition();
                skipWhiteSpaces();
            }
            return new JSONObject(out);
        }

        private JSONArray parseJSONArray() {
            incrementPosition();
            skipWhiteSpaces();
            List<Object> list = new ArrayList<>();
            list.add(parseValue());
            incrementPosition();
            skipWhiteSpaces();

            while (currentChar() != ']') {
                if (currentChar() == ',') {
                    incrementPosition();
                    skipWhiteSpaces();
                    list.add(parseValue());
                } else throw exception("',' or ']' expected");
                incrementPosition();
                skipWhiteSpaces();
            }
            return new JSONArray(list);
        }

        private RuntimeException exception(String message) {
            return new RuntimeException(new ParseException(message, pos));
        }
    }

    public static final class Null {
        @Override
        public String toString() {
            return "null";
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public static void main(String[] args) {
        JSONObject obj = new JSONObject("""
                          { "object":   {"a"  :59}, "a": [42 ,  "deine mudda"],
                          "key": 42.69, "key2": null, "key3":  false, "key4":true}
                """);
        System.out.println(obj);
    }
}