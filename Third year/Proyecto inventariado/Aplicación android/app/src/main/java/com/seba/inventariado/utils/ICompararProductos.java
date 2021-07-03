package com.seba.inventariado.utils;

import com.seba.inventariado.model.Producto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface ICompararProductos extends Comparator<Producto> {

    class ComparadorNombreA_Z implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return producto1.getNombre().compareTo(producto2.getNombre());
        }
    }

    class ComparadorNombreZ_A implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return producto2.getNombre().compareTo(producto1.getNombre());
        }
    }

    class ComparadorFechaReciente implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return StringUtils.compare(producto1.getUpdateDate().toString(), producto2.getUpdateDate().toString());
        }
    }

    class ComparadorFechaAntigua implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return StringUtils.compare(producto2.getUpdateDate().toString(), producto1.getUpdateDate().toString());
        }
    }

    class ComparadorCantidadMayorMenor implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return Integer.parseInt(producto2.getCantidad()) - (Integer.parseInt(producto1.getCantidad()));
        }
    }

    class ComparadorCantidadMenorMayor implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return Integer.parseInt(producto1.getCantidad()) - (Integer.parseInt(producto2.getCantidad()));
        }
    }

    class ComparadorValorTotalMayorMenor implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return Integer.parseInt(producto2.getPrecio()) - (Integer.parseInt(producto1.getPrecio()));
        }
    }

    class ComparadorValorTotalMenorMayor implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return Integer.parseInt(producto1.getPrecio()) - (Integer.parseInt(producto2.getPrecio()));
        }
    }

    class ComparadorAlarmaReciente implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return StringUtils.compare(producto1.getMinLvlAlertDate().toString(), producto2.getMinLvlAlertDate().toString());
        }
    }

    class ComparadorAlarmaAntigua implements Comparator<Producto> {
        @Override
        public int compare(@NotNull Producto producto1, @NotNull Producto producto2) {
            return StringUtils.compare(producto2.getMinLvlAlertDate().toString(), producto1.getMinLvlAlertDate().toString());
        }
    }

}
