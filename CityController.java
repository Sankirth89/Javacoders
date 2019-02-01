package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CityController {

    @RequestMapping(value = "/connected", method = RequestMethod.GET)
    public String city(@RequestParam String origin, @RequestParam String destination) {
        
        File file;
        String connected = "no";
        try {
            file = ResourceUtils.getFile("classpath:city.txt");
            String content = new String(Files.readAllBytes(file.toPath()));
            
            connected = findConnected(content, origin, destination);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        return connected;
    }

    private String findConnected(String content, String origin, String destination) {
        
        String connected = "no";
        String[] roads = content.toLowerCase().split("\r\n|\n");
        HashMap<String, Integer> cityMap = new HashMap<>();
        int cityCount = 0;
        for(int i=0;i<roads.length;i++) {
            String split[] = roads[i].split(", ");
            for(int j=0;j<split.length;j++) {
                if(!cityMap.containsKey(split[j])) {
                    cityMap.put(split[j], cityCount++);
                }
            }
        }
        
        Graph graph = new Graph(cityMap.size());
        for(int i=0;i<roads.length;i++) {
            String split[] = roads[i].split(", ");
            graph.addEdge(cityMap.get(split[0]), cityMap.get(split[1]));
            graph.addEdge(cityMap.get(split[1]), cityMap.get(split[0]));
        }
        
        if (graph.isReachable(cityMap.get(origin.toLowerCase()), cityMap.get(destination.toLowerCase()))) 
            connected = "yes";
        else
            connected = "no";
        
        return connected;
    }
}

