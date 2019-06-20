/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONUtils {
    public static final String EMPTY_JSON = "{}";
    
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    
    public static String toJSON(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException ex) {
            System.out.println("JSON parsing failure");
            Logger.getLogger(JSONUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return EMPTY_JSON;
    }
    
    // Not needed for now
    public static Object fromJSON(String json) {
        return null;
    }
}
