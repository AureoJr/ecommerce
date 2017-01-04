var app = angular.module('ecommerce-app', ['ngRoute']);

app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "/tpl/home.html",
            controller : "HomeController"
        })
        .when("/about", {
            templateUrl : "/tpl/home.html",
            controller : "AboutController"
        })
        .when("/store/:storeName", {
            templateUrl : "/tpl/store.html",
            controller : "StoreController"
        });
}).controller('CategoryController', function($http) {
    var self = this;
    $http.get('/categories').then(function(response) {
        self.categories = response.data.categories ;
    })

}).controller('StoreController', ['$routeParams', function StoreController($routeParams,$http) {

}]).controller('AboutController', function ($http) {

}).controller('HomeController', function ($http) {

})
;


