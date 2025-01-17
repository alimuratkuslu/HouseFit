package com.alikuslu.housefit.demo.dto;

import com.alikuslu.housefit.demo.model.TipCategory;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class TipDto {

    private String title;
    private String content;

    @NotNull
    private TipCategory category;
}
