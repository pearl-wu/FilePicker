var exec = require('cordova/exec');

exports.start = function(options) {
    if(typeof options == "undefined"){
        options = {};
    }
    exec(function(){}, function(){}, "filepicker", "start", [options]);
};

exports.get = function(message, win, fail){
    exec(win, fail, "filepicker", "get", [message]);
}

exports.find = function(message, win, fail){
    exec(win, fail, "filepicker", "find", [message]);
}

exports.openfile = function(message, win, fail){
    exec(win, fail, "filepicker", "openfile", [message]);
}

exports.openweb = function(message, win, fail){
    exec(win, fail, "filepicker", "openweb", [message]);
}

exports.chooespicture = function(success, fail, options) {
    if (!options) {
        options = {};
    }    
    var params = {
        type: options.type ? options.type : "multiple",
        limit: options.limit ? options.limit : 15,
        cancelButtonText: options.cancelButtonText ? options.cancelButtonText : "Cancel",
        okButtonText: options.okButtonText ? options.okButtonText : "Done",
        titleText: options.titleText ? options.titleText : "Gallery",
        errorMessageText: options.errorMessageText ? options.errorMessageText : "Max limit reached!"
    };
    return cordova.exec(success, fail, "MultiImageSelector", "getPictures", [params]);
};

exports.chooesfiile = function(message, win, fail){
    exec(win, fail, "filepicker", "chooesfiile", []);
}
