;(function () {
    'use strict';

    angular.module('ecommerceApp.homeController', ['ngMaterial'])
        .controller('HomeController',['$http', '$mdToast', HomeController]);

    function HomeController ($http, $mdToast) {
        var vm = this;
        vm.products = [];
        vm.message = "Novo produto adicionado no carri                                                                                                                                                                                                                                                  nho";

        vm.addProductToCart = addProductToCart;

        $http.get('/product/Carros').then(function (response) {
            vm.products = response.data.products;
        });


        function addProductToCart(product) {

            $http.post("cart", product).then(function (response) {
                vm.message = response.data.message;
            });

            $mdToast.show(
                $mdToast.simple()
                    .textContent(vm.message)
                    .position('bottom right')
                    .hideDelay(3000)
            );
        }
    }

})();