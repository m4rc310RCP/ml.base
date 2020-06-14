/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.rcp.ui.utils.streaming;

import java.util.EventListener;

/**
 *
 * @author Marcelo
 */
public interface MListener extends EventListener {
    void eventChange(MEvent event);
}
