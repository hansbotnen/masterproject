package com.example.demo;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.util.ArrayList;
import java.util.List;

public class SseEmitters {
    private final List<SseEmitter> emitterList = new ArrayList<>();

    public SseEmitter add(SseEmitter sseEmitter){
        emitterList.add(sseEmitter);
        sseEmitter.onTimeout(()-> sseEmitter.complete());
        sseEmitter.onCompletion(()-> this.emitterList.remove(sseEmitter));
        return sseEmitter;
    }

    public void send(Object object){
        for(SseEmitter emitter: emitterList){
            try{
                emitter.send(object);
            } catch (Exception e) {
                emitter.completeWithError(e);
                emitterList.remove(emitter);
            }
        }


    }


}
