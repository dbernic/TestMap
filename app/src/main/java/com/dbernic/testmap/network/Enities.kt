package com.dbernic.testmap.network

class ServerError(val message: String)

class Request(val elevation: Boolean, val points_encoded: Boolean, val vehicle: String, val points: ArrayList<Array<Double>>)

class Result(val paths: ArrayList<Path>)

class Path(val points: Points)

class Points(val coordinates: ArrayList<ArrayList<Double>>)

