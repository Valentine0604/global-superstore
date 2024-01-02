package com.ltp.globalsuperstore;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SuperstoreController {

    private List<Item> items = new ArrayList<>();

    @GetMapping("/")
    public String getForm(Model model, @RequestParam(required = false) String id){
        model.addAttribute("categories", Constants.CATEGORIES);
        model.addAttribute("item", getItemIndex(id) == Constants.NOT_FOUND ? new Item() : items.get(getItemIndex(id)));
        return "form";
    }

    @PostMapping("/submitItem")
    public String handleSubmit(Item item, RedirectAttributes redirectAttributes){
        int index = getItemIndex(item.getId());
        String status = Constants.SUCCESS_STATUS;
        if(index == Constants.NOT_FOUND){
            items.add(item);
        }
        else if(within5Days(item.getDate(), items.get(index).getDate())){
            items.set(index, item);
        }
        else{
            status = Constants.FAILED_STATUS;
        }
        redirectAttributes.addFlashAttribute("status", status);
        return "redirect:/inventory";
    }

    @GetMapping("/inventory")
    public String getInventory(Model model){
        model.addAttribute("items", items);
        return "inventory";
    }

    public Integer getItemIndex(String id){
        for(int i=0; i<items.size(); i++){
            if(items.get(i).getId().equals(id)){
                return i;
            }
        }
        return Constants.NOT_FOUND;
    }

    public boolean within5Days(java.util.Date date, java.util.Date date2){
        long diff = Math.abs(date.getTime() - date2.getTime());
        return (int)(TimeUnit.MICROSECONDS.toDays(diff)) <= 5;
    }

    
}
