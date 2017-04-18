

angular.module('frontendServices', [])
    .service('UserService', ['$http','$q', '$cookieStore', function($http, $q, $cookieStore) {
        return {
            logout: function () {
                globalUserData = undefined;
                $http({
                    method: 'POST',
                    url: '/api/logout'
                })
                .then(function (response) {
                    if (response.status == 200) {
                        $cookieStore.remove("loggedIn");
                        window.location.href = "/";
                    } else {
                        console.log("error.logout");
                    }
                });
            },
            getUserData: function(){
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: '/api/user',
                    responseType:'json'
                })
                .then(function (response) {
                    if (response.status == 200 && response.data) {
                        deferred.resolve(response.data);
                    } else {
                        deferred.reject("error.userdata");
                    }
                });
                return deferred.promise;
            }
        };
    }])
    .service('DriveSearchService', function() {
        var from, to;
        var setFrom = function(f){
            from = f;
        };
        var setTo = function(t){
            to = t;
        };
        var getTo = function(){
            return to;
        };
        var getFrom = function(){
            return from;
        };
        return {
            setFrom: setFrom,
            setTo: setTo,
            getFrom: getFrom,
            getTo: getTo
        };
    })
    .service('DriveService', ['$http', '$q', function($http, $q) {
        return {
            findRides: function(source, destination){
                var searchDriveDTO = {from: source, to: destination};
                var deferred = $q.defer();
                $http({
                    method: 'POST',
                    url: '/api/drive/find',
                    data: searchDriveDTO,
                    headers: {
                        "Content-Type": "application/json;charset=UTF-8"
                    }
                })
                .then(function (response) {
                    if (response.status == 200) {
                        deferred.resolve(response.data);
                    } else {
                        deferred.reject("error.rides.search");
                    }
                });

                return deferred.promise;
            },
            getMyRides: function(){
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: '/api/drive',
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "text/plain, application/json"
                    }
                })
                .then(function (response) {
                    if (response.status == 200) {
                        deferred.resolve(response.data);
                    } else {
                        deferred.reject("error.rides.search");
                    }
                });

                return deferred.promise;
            },
            addRide: function(ride) {
                var deferred = $q.defer();
                $http({
                    method: 'POST',
                    url: '/api/drive',
                    data: ride,
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "text/plain, application/json"
                    }
                })
                .then(function (response) {
                    if (response.status == 200) {
                        deferred.resolve("success.ride.saved");
                    } else {
                        deferred.reject("error.saving.ride");
                    }
                });

                return deferred.promise;
            },
            getRideById: function(rideId){
                var deferred = $q.defer();
                $http({
                    method: 'GET',
                    url: '/api/drive/' + rideId,
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "text/plain, application/json"
                    }
                })
                .then(function (response) {
                    if (response.status == 200) {
                        deferred.resolve(response.data);
                    } else {
                        deferred.reject("error.getting.ride");
                    }
                });

                return deferred.promise;
            },
            deleteRide: function(rideId){
                var deferred = $q.defer();
                $http({
                    method: 'DELETE',
                    url: '/api/drive',
                    data: rideId,
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "text/plain, application/json"
                    }
                })
                .then(function (response) {
                    if (response.status == 200) {
                        deferred.resolve("success.ride.deleted");
                    } else {
                        deferred.reject("error.deleting.ride");
                    }
                });

                return deferred.promise;
            }
        }
    }]);