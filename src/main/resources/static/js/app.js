var app = angular.module('ecommerce-app', ['ngRoute', 'ngMaterial']);

app.config(function($routeProvider,$httpProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "/tpl/home.html",
            controller : "HomeController",
            controllerAs : "home"
        })
        .when("/about", {
            templateUrl : "/tpl/about.html",
            controller : "AboutController",
            controllerAs : "about"
        }).when("/cart", {
            templateUrl : "/tpl/cart.html",
            controller : "CartController",
            controllerAs : "cart"
        })
        .when("/store/:storeName", {
            templateUrl : "/tpl/store.html",
            controller  : "StoreController",
            controllerAs : "store"
        }).otherwise("/");

    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

}).controller('CategoryController', function($http, $scope, $location) {
    var self = this;
    $http.get('/categories').then(function(response) {
        self.categories = response.data.categories ;
    });

    $scope.goTo = function ( path ) {
        $location.path( path );
    };

}).controller('StoreController', ['$routeParams', function StoreController($routeParams,$http) {
    var self = this;
    $http.get('/categories').then(function(response) {
        self.categories = response.data.categories ;
    })

}]).controller('AboutController', function ($http) {

}).controller('HomeController', function ($http, $scope, $mdToast) {
    var self = this;

    $http.get('/product/Carros').then(function(response) {
        self.products = response.data.products ;
    });

    $scope.addProductToCart = function(product) {

        $http.post("cart", product).then(function (response) {
            self.message = response.data.message;
        });

        $mdToast.show(
            $mdToast.simple()
                .textContent(self.message)
                .position('bottom right')
                .hideDelay(3000)
        );
    };

}).controller("CartController", function($http,$mdToast){
    var self = this;
    self.isOpen = false;

    $http.get("/cart").then(function (response) {
        self.products = response.data.products;
    });
    $http.get("/cart/checkout").then(function (response) {

        $mdToast.show(
            $mdToast.simple()
                .textContent(response.data.message)
                .position('bottom right')
                .hideDelay(3000)
        );
    })
})
;
