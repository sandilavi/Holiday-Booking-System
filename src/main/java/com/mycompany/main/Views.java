package com.mycompany.main;

public class Views {
    public static class RoomAndHotel {}
    public static class OnlyForRoom extends RoomAndHotel {}
    public static class OnlyForHotel extends RoomAndHotel {}
    public static class IgnoreHotelId extends RoomAndHotel {}
}
