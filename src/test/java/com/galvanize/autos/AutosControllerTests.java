package com.galvanize.autos;

public class AutosControllerTests {

    // Get: /api/autos
        // GET: /api/autos Returns 204 if no autos in the db
        // GET: /api/autos Returns list of all autos
        // GET: /api/autos?color=red returns red cars
        // GET: /api/autos?make=ford returns Ford cars
        // GET: /api/autos?color=red&make=ford returns red Ford cars

    // POST: //api/autos
        // POST: /api/autos returns status code 400
        // POST: /api/autos returns status code 200 and created auto

    // GET: /api/autos/{vin}
        // GET: /api/autos/{vin} returns status code 204 Auto not found
        // GET: /api/autos/{vin} returns status code 200 and the requested auto

    // PATCH: /api/autos/{vin}
        // PATCH: /api/autos/{vin} returns status code 204 Auto not found
        // PATCH: /api/autos returns status code 400
        // PATCH: /api/autos/{vin} returns status code 200 and patched auto

    // DELETE: /api/autos/{vin}
        // DELETE: /api/autos/{vin} returns status code 204 Auto not found
        // DELETE: /api/autos/{vin} returns status code 202, delete request accepted





}
