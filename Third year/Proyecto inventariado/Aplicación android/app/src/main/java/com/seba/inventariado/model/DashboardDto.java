package com.seba.inventariado.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DashboardDto implements Parcelable {
    String items;
    String tags;
    String totalQuantity;
    String totalValue;
    List<Producto> recentEditProducts;

    public DashboardDto() {
    }

    public DashboardDto(String items, String tags, String totalQuantity, String totalValue, List<Producto> recentEditProducts) {
        this.items = items;
        this.tags = tags;
        this.totalQuantity = totalQuantity;
        this.totalValue = totalValue;
        this.recentEditProducts = recentEditProducts;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    public List<Producto> getRecentEditProducts() {
        return recentEditProducts;
    }

    public void setRecentEditProducts(List<Producto> recentEditProducts) {
        this.recentEditProducts = recentEditProducts;
    }

    /**
     * codigo generado por http://www.parcelabler.com/
     **/
    protected DashboardDto(Parcel in) {
        items = in.readString();
        tags = in.readString();
        totalQuantity = in.readString();
        totalValue = in.readString();
        if (in.readByte() == 0x01) {
            recentEditProducts = new ArrayList<>();
            in.readList(recentEditProducts, Producto.class.getClassLoader());
        } else {
            recentEditProducts = null;
        }
    }

    /**
     * codigo generado por http://www.parcelabler.com/
     **/
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * codigo generado por http://www.parcelabler.com/
     **/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(items);
        dest.writeString(tags);
        dest.writeString(totalQuantity);
        dest.writeString(totalValue);
        if (recentEditProducts == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(recentEditProducts);
        }
    }

    /**
     * codigo generado por http://www.parcelabler.com/
     **/
    public static final Parcelable.Creator<DashboardDto> CREATOR = new Parcelable.Creator<DashboardDto>() {
        @Override
        public DashboardDto createFromParcel(Parcel in) {
            return new DashboardDto(in);
        }

        @Override
        public DashboardDto[] newArray(int size) {
            return new DashboardDto[size];
        }
    };

}
