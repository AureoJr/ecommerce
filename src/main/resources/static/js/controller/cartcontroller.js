;(function () {
    'use strict';

    angular.module('ecommerceApp.cartController', ['ngMaterial'])
        .controller("CartController", ['$http', '$mdToast', '$mdDialog', CartController]);

    function CartController ($http, $mdToast, $mdDialog) {
        var vm = this;
        vm.opened = false;
        vm.products = [];
        vm.finalize = finalize;
        vm.removeProductFromCart = removeProductFromCart;

        $http.get("/cart").then(function (response) {
            vm.products = response.data.products;
        });

        function removeProductFromCart(){
            console.log('limpando items');
            vm.products.filter(function (item) {
                console.log('limpando item: ' + item.name );
                return item.checked;
            }).forEach(function (item) {
                vm.products.splice(vm.products.indexOf(item),1);
                console.log('request para: /cart/'+item.id);
                $http.delete("/cart/"+item.id);
            })
        }

        function finalize(ev) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.confirm()
                .title('Deseja finalize o pedido ?')
                .textContent('Seu pedifo possui alguns items, gostaria de finalizá-lo.')
                .targetEvent(ev)
                .ok('Sim')
                .cancel('Preciso compra mais ');

            $mdDialog.show(confirm).then(function () {
                $http.post("cart/checkout").then(function (response) {
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent(response.data.message)
                            .position('bottom right')
                            .hideDelay(3000)
                    );

                });
            });

            $http.get("/cart/checkout").then(function (response) {

                $mdToast.show(
                    $mdToast.simple()
                        .textContent(response.data.message)
                        .position('bottom right')
                        .hideDelay(3000)
                )
            });
        }
    }
})();