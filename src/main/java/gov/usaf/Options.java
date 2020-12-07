package gov.usaf;

import java.util.HashMap;

public class Options {
    public static HashMap splitArgs(String[] args) throws Exception {
        HashMap<String,String> argOptions = new HashMap<>();
        for (int i = 0; i < args.length; i+=2) {
            if (i+1 < args.length) {
                if (!args[i].startsWith("-")) {
                    throw new Exception("Invalid option");
                }
                argOptions.put(args[i].substring(1),args[i+1]);
            }
        }
        return argOptions;
    }

}
