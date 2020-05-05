package com.example.bilet1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "citate")
public class Citat implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "autor")
    private String autor;
    @ColumnInfo(name = "text")
    private String text;
    @ColumnInfo(name = "numarAprecieri")
    private Integer numarAprecieri;
    @ColumnInfo(name = "categorie")
    private String categorie;

    public Citat(long id, String autor, String text, Integer numarAprecieri, String categorie) {
        this.id = id;
        this.autor = autor;
        this.text = text;
        this.numarAprecieri = numarAprecieri;
        this.categorie = categorie;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Ignore
    public Citat(String autor, String text, Integer numarAprecieri, String categorie) {
        this.autor = autor;
        this.text = text;
        this.numarAprecieri = numarAprecieri;
        this.categorie = categorie;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNumarAprecieri() {
        return numarAprecieri;
    }

    public void setNumarAprecieri(Integer numarAprecieri) {
        this.numarAprecieri = numarAprecieri;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    protected Citat(Parcel in) {
        autor = in.readString();
        text = in.readString();
        if (in.readByte() == 0) {
            numarAprecieri = null;
        } else {
            numarAprecieri = in.readInt();
        }
        categorie = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(autor);
        dest.writeString(text);
        if (numarAprecieri == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numarAprecieri);
        }
        dest.writeString(categorie);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Citat> CREATOR = new Creator<Citat>() {
        @Override
        public Citat createFromParcel(Parcel in) {
            return new Citat(in);
        }

        @Override
        public Citat[] newArray(int size) {
            return new Citat[size];
        }
    };

    @Override
    public String toString() {
        return "Citat{" +
                "autor='" + autor + '\'' +
                ", text='" + text + '\'' +
                ", numarAprecieri=" + numarAprecieri +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
