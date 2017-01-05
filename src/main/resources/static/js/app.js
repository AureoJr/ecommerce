var app = angular.module('ecommerce-app', ['ngRoute', 'ngMaterial']);

app.config(function($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl : "/tpl/home.html",
            controller : "HomeController",
            controllerAs : "home"
        })
        .when("/about", {
            templateUrl : "/tpl/about.html",
            controller : "about"
        }).when("/cart", {
            templateUrl : "/tpl/cart.html",
            controller : "about"
        })
        .when("/store", {
            templateUrl : "/tpl/cart.html",
            controller : "store"
        });
}).controller('CategoryController', function($http) {
    var self = this;
    $http.get('/categories').then(function(response) {
        self.categories = response.data.categories ;
    })

}).controller('StoreController', ['$routeParams', function StoreController($routeParams,$http) {
    var self = this;
    $http.get('/categories').then(function(response) {
        self.categories = response.data.categories ;
    })

}]).controller('AboutController', function ($http) {

}).controller('HomeController', function ($http,$scope, $mdToast) {
    var self = this;

    $http.get('/product/Carros').then(function(response) {
        self.products = response.data.products ;
    });

    $scope.addProductToCart = function(product) {

        $mdToast.show(
            $mdToast.simple()
                .textContent(product.name + " Adicionado ao carrinho!")
                .position('bottom right')
                .hideDelay(3000)
        );
    };

})
;
