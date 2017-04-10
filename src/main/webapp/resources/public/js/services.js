angular.module('ServicesModule', []).factory('SearchService', ['$http', '$q', function($http, $q) {
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
    var autocomplete = function(searchStr){
        var deferred = $q.defer();
        $http({
            method: 'POST',
            url: '/api/autocomplete',
            data: searchStr,
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            }
        })
        .then(function (response) {
            if (response.status == 200) {
                deferred.resolve(response.data);
            } else {
                deferred.reject("Error while autocompleting: " + response.data);
            }
        });

        return deferred.promise;
    };
    return {
        setFrom: setFrom,
        setTo: setTo,
        getFrom: getFrom,
        getTo: getTo,
        autocomplete: autocomplete
    };
}])
.factory('DriveService', function () {
    var getDrives = function (from, to) {
        //var deferred = $q.defer();
        //$http.get("/drive/find?from=Rīga&to=Daugavpils").success(function(data, status) {
        //    if(status == 200){
        //        $scope.hello = data;
        //    }
        //});
        return {"aleksandr": "burda6ev"};
    };
    return {getDrives: getDrives}
});