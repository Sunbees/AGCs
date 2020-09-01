package com.sun.controller;

import com.sun.algorithm.ACO;
import com.sun.algorithm.GeneticAlgorithm;
import com.sun.algorithm.SimulatedAnnealing;
import com.sun.data.Data;
import com.sun.pojo.Locations;
import com.sun.pojo.Population;
import com.sun.pojo.Route;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class TestController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/query")
    public String query() {
        return "query";
    }

    @PostMapping("/query")
    public String test(Locations locations, @RequestParam("algorithmType") Integer algorithmType, Model model) {
        Data.init(locations);
        Route best = null;
        if (algorithmType == 0) {
            SimulatedAnnealing sa = new SimulatedAnnealing();
            sa.initRoute();
            best = sa.anneal();
            //System.out.println(best);
            //System.out.println("最短距离：" + best.getDistance());
            model.addAttribute("algorithm","模拟退火");
        } else if (algorithmType == 1) {
            GeneticAlgorithm ga = new GeneticAlgorithm();
            Population population = new Population(200);
            best= ga.run(population);
            //System.out.println(best);
            //System.out.println(best.getDistance());
            model.addAttribute("algorithm","遗传算法");
        } else if (algorithmType == 2) {
            ACO aco = new ACO();
            aco.init();
            best = aco.solve();
            model.addAttribute("algorithm","蚁群算法");
        }
        model.addAttribute("locations",best.getLocationList());
        model.addAttribute("distance",best.getDistance());
        return "show";
    }
}
