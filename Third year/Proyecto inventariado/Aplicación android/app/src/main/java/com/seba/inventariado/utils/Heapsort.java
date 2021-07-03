package com.seba.inventariado.utils;

import com.seba.inventariado.model.Producto;

import java.util.Comparator;
import java.util.List;


/**
 * @author rice2007 editado por sebastian morgado
 * @link https://gist.github.com/rice2007/9221749
 */
public class Heapsort {

    private static int tamanoHeap;

    public static List<Producto> heapSort(List<Producto> listaProductos, Comparator<? super Producto> comparator) {
        construirHeap(listaProductos, comparator);

        for (int i = listaProductos.size() - 1; i >= 0; i--) {
            Producto aux = listaProductos.get(0);
            listaProductos.set(0, listaProductos.get(i));
            listaProductos.set(i, aux);
            tamanoHeap--;
            heapify(listaProductos, 0, comparator);
        }
        return listaProductos;
    }

    private static void construirHeap(List<Producto> listaProductos, Comparator<? super Producto> comparator) {
        tamanoHeap = listaProductos.size();
        for (int i = (int) Math.floor((float) (listaProductos.size()) / 2) - 1; i >= 0; i--) {
            heapify(listaProductos, i, comparator);
        }
    }

    private static void heapify(List<Producto> listaProductos, int i, Comparator<? super Producto> comparator) {
        int izq = getNodoIzq(i);
        int der = getNodoDer(i);
        int nodo = ((izq <= tamanoHeap - 1) && (comparator.compare(listaProductos.get(izq), listaProductos.get(i)) > 0)) ? izq : i;

        if ((der <= tamanoHeap - 1) && (comparator.compare(listaProductos.get(der), listaProductos.get(nodo)) > 0))
            nodo = der;

        if (nodo != i) {
            Producto aux = listaProductos.get(i);
            listaProductos.set(i, listaProductos.get(nodo));
            listaProductos.set(nodo, aux);
            heapify(listaProductos, nodo, comparator);
        }
    }

    private static int getNodoIzq(int i) {
        return (i * 2);
    }

    private static int getNodoDer(int i) {
        return (i * 2 + 1);
    }
}