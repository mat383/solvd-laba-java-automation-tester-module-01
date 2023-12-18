package com.solvd.laba.homework02.exercise01.util;

import java.util.EnumMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArgumentsParser {
    private final Map<Flags, String> optionsMap;

    public ArgumentsParser(String[] args) {
        this.optionsMap = new EnumMap<>(Flags.class);
        Flags lastFlag = null;

        // parse args
        for (String arg : args) {
            if (lastFlag == null) {
                lastFlag = this.findFlag(arg);
                this.optionsMap.put(lastFlag, "");
                // remember last flag if some argument is expected
                lastFlag = lastFlag.haveValue()
                        ? lastFlag
                        : null;
            } else {
                // retrieve value associated with lastFlag
                this.optionsMap.put(lastFlag, arg);
                lastFlag = null;
            }
        }
    }

    public boolean flagPresent(Flags flag) {
        return this.optionsMap.containsKey(flag);
    }

    public String getFlagValue(Flags flag) {
        return this.optionsMap.get(flag);
    }

    private Flags findFlag(String argument) {
        if (argument.startsWith("--")) {
            return Flags.ofLongName(argument.substring(2));
        } else if (argument.startsWith("-")) {
            return Flags.ofShortName(argument.substring(1));
        }
        throw new IllegalArgumentException("Unrecognized parameter: " + argument);
    }


    public enum Flags {
        HELP(false, "h", "help"),
        PERSON(true, "p", "person"),
        UPCOMING_APPOINTMENTS(false, "a", "appointments");

        /**
         * whether flag will have value (ie. --flag value)
         */
        private final boolean haveValue;
        private final String shortName;
        private final String longName;

        Flags(boolean haveValue, String shortName, String longName) {
            this.haveValue = haveValue;
            this.shortName = shortName;
            this.longName = longName;
        }

        public boolean haveValue() {
            return this.haveValue;
        }

        public String getShortName() {
            return this.shortName;
        }

        public String getLongName() {
            return this.longName;
        }

        public static Flags ofLongName(String wantedFlag) {
            for (Flags flag : Flags.values()) {
                if (wantedFlag.equals(flag.getLongName())) {
                    return flag;
                }
            }
            throw new NoSuchElementException("No such long flag: " + wantedFlag);
        }

        public static Flags ofShortName(String wantedFlag) {
            for (Flags flag : Flags.values()) {
                if (wantedFlag.equals(flag.getShortName())) {
                    return flag;
                }
            }
            throw new NoSuchElementException("No such short flag: " + wantedFlag);
        }
    }
}
