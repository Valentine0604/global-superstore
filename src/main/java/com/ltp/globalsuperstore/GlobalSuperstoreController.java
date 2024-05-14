package com.ltp.globalsuperstore;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GlobalSuperstoreController {

    private List<Item> items = new ArrayList<>();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id){

        int match = getItemIndex(id);

        model.addAttribute("categories", Constants.CATEGORIES);
        model.addAttribute("item", match == Constants.NOT_FOUND ? new Item() : items.get(match));
        return "form";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model){
        model.addAttribute("items", items);
        return "inventory";
    }

    @PostMapping("/submitItem")
    public String handleSubmit(Item item) {

        int match = getItemIndex(item.getId());

        if(match == Constants.NOT_FOUND){
            items.add(item);
        }
        else{
            items.set(match, item);
        }

        return "redirect:/inventory";
    }
    
    public Integer getItemIndex(String id){
        for(int i=0; i<items.size(); i++){
            if(items.get(i).getName().equals(id)) return i;
        }

        return Constants.NOT_FOUND;
    }
}
