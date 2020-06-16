package model;

import com.example.demo.Subscription;
import lombok.Data;

import java.util.List;

@Data
public class SubscriberList {
    List<Subscription> subscribers;
    public SubscriberList(){}
    public SubscriberList(List<Subscription> list){
        subscribers=list;
    }
}
