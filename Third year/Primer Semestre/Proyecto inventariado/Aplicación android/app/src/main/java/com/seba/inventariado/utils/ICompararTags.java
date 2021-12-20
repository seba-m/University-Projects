package com.seba.inventariado.utils;

import com.seba.inventariado.model.Tag;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface ICompararTags extends Comparator<Tag> {

    class ComparadorNombreA_Z implements Comparator<Tag> {
        @Override
        public int compare(@NotNull Tag producto1, @NotNull Tag producto2) {
            return producto1.getNombre().compareTo(producto2.getNombre());
        }
    }

    class ComparadorNombreZ_A implements Comparator<Tag> {
        @Override
        public int compare(@NotNull Tag producto1, @NotNull Tag producto2) {
            return producto2.getNombre().compareTo(producto1.getNombre());
        }
    }
}
