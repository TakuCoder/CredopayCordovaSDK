
function PayPalMobile() {}


PayPalMobile.prototype.init = function(clientIdsForEnvironments, completionCallback) {
  var failureCallback = function(error) {
    console.log(error);
  };

  cordova.exec(completionCallback, failureCallback, "PayPalMobile", "init", [clientIdsForEnvironments]);
};


module.exports = new PayPalMobile();
