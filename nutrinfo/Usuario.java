package com.app.nutrinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by NaianaPC on 29/09/2017.
 */

public class Usuario implements Parcelable {

    private String nome;
    private String email;
    private String senha;
    private Character sexo;
    private Date dn;

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Character getSexo() {
        return sexo;
    }
    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }
    public Date getDn() {
        return dn;
    }
    public void setDn(Date dn) {
        this.dn = dn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nome);
        dest.writeString(this.email);
        dest.writeString(this.senha);
        dest.writeSerializable(this.sexo);
        dest.writeLong(this.dn != null ? this.dn.getTime() : -1);
    }

    protected Usuario(Parcel in) {
        this.nome = in.readString();
        this.email = in.readString();
        this.senha = in.readString();
        this.sexo = (Character) in.readSerializable();
        long tmpDn = in.readLong();
        this.dn = tmpDn == -1 ? null : new Date(tmpDn);
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}