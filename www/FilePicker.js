var exec = require('cordova/exec');

exports.start = function(options) {
    if(typeof options == "undefined"){
        options = {};
    }

    exec(function(){}, function(){}, "filepicker", "start", [options]);
};
