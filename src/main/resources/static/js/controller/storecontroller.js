;(function () {
    'use strict';

    angular.module('ecommerceApp', ['ngMaterial'])
        .controller('StoreController',['$http', '$mdToast', StoreController]);

    function StoreController ($http, $mdToast) {
        var vm = this;
        vm.products = [];
        vm.message = "";

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