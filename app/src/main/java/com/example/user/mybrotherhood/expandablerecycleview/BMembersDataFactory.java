package com.example.user.mybrotherhood.expandablerecycleview;

import com.example.user.mybrotherhood.expandablerecycleview.multicheck.MultiCheckMember;

import java.util.Arrays;
import java.util.List;

public class BMembersDataFactory {



  public static List<MultiCheckMember> makeMultiCheckMembers() {
    return Arrays.asList(makeMultiCheckMemberTommy(),
        makeMultiCheckMemberVlad(),
        makeMultiCheckMemberNicolas());
  }





  public static MultiCheckMember makeMultiCheckMemberTommy() {
    return new MultiCheckMember("Tommy", makeTommyCategories());
  }


  public static List<Category> makeTommyCategories() {
    Category Rent = new Category("Rent", true);
    Category Food = new Category("Food", false);
    Category Water = new Category("Water", false);
    Category Netflix = new Category("Netflix", true);

    return Arrays.asList(Rent, Food, Water, Netflix);
  }



  public static MultiCheckMember makeMultiCheckMemberVlad() {
    return new MultiCheckMember("Vlad", makeVladCategories());
  }



  public static List<Category> makeVladCategories() {
    Category GymEquipment = new Category("Gym Equipment", true);
    Category ProteinBars = new Category("Protein Bars", true);
    Category MusicStuff = new Category("Music Stuff", false);

    return Arrays.asList(GymEquipment, ProteinBars, MusicStuff);
  }


  public static MultiCheckMember makeMultiCheckMemberNicolas() {
    return new MultiCheckMember("Nicolas", makeNicolasCategories());
  }


  public static List<Category> makeNicolasCategories() {
    Category Games = new Category("Games", false);
    Category Tea = new Category("Tea", true);
    Category JavaBooks = new Category("Java Books", false);
    Category Computers = new Category("Computers", false);

    return Arrays.asList(Games, Tea, JavaBooks, Computers);
  }



}

