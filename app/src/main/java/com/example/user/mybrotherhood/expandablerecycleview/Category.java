package com.example.user.mybrotherhood.expandablerecycleview;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

  private String name;
  private boolean isFavorite;

  public Category(String name, boolean isFavorite) {
    this.name = name;
    this.isFavorite = isFavorite;
  }

  protected Category(Parcel in) {
    name = in.readString();
  }

  public String getName() {
    return name;
  }

  public boolean isFavorite() {
    return isFavorite;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category)) return false;

    Category category = (Category) o;

    if (isFavorite() != category.isFavorite()) return false;
    return getName() != null ? getName().equals(category.getName()) : category.getName() == null;

  }

  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result = 31 * result + (isFavorite() ? 1 : 0);
    return result;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override
    public Category createFromParcel(Parcel in) {
      return new Category(in);
    }

    @Override
    public Category[] newArray(int size) {
      return new Category[size];
    }
  };
}

