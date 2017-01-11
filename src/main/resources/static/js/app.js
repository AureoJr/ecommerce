;(function () {
    'use strict';

    angular.module('ecommerceApp',[
        'ngRoute',
        'ecommerceApp.homeController',
        'ecommerceApp.cartController',
        'ecommerceApp.editedNavBar'
    ]);
    angular.module('ecommerceApp')
        .config(['$routeProvider', '$httpProvider' , configRoutes]);


    function configRoutes($routeProvider, $httpProvider) {
        $routeProvider
            .when("/", {
                templateUrl: "/tpl/home.html",
                controller: "HomeController",
                controllerAs: "home"
            }).when("/cart", {
                templateUrl: "/tpl/cart.html",
                controller: "CartController",
                controllerAs: "cart"
            })
            .otherwise({
                redirectTo: "/"
            });
        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    }

})();
