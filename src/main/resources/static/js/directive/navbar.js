;(function(){
   'use strict';

   angular.module('ecommerceApp.editedNavBar', ['ngMaterial'])
        .directive('editedNavBar', ['$http', '$location', navBarDirective]);

   function navBarDirective($http, $location) {

       return {
           restrict: 'E',
           templateUrl: '/tpl/nav-bar.html',
           controller: function ($http,$location) {
               var vm = this;

               vm.categories = [];
               vm.locationPath = $location.path().split("/");
               vm.goTo = gotTo;

               function gotTo(path) {
                   $location.path(path);
               }

               $http.get('/categories').then(function (response) {
                   vm.categories = response.data.categories;
               })

           },
           controllerAs: 'nav'
       };
   }
})();