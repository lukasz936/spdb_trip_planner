package com.tripplanner.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteRequestData {

    public static int FIND_ROUTE = 1;
    public static int FIND_SECTION = 2;
    public int requestCounter = 0;

    public List<Route> routes = new ArrayList<>();

}
