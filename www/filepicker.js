var exec = require('cordova/exec');

exports.start = function(options) {
    if(typeof options == "undefined"){
        options = {};
    }
    exec(function(){}, function(){}, "filepicker", "start", [options]);
};

exports.version = function(win, fail){
    exec(win, fail, "filepicker", "version", []);
}

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

exports.choosepicture = function(win, fail, options) {
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
    exec(win, fail, "filepicker", "choosepicture", [params]);
};

exports.chooesfiile = function(message, win, fail){
    if (!message) {
        message = [];
    } 
    
    exec(win, fail, "filepicker", "chooesfiile", message);
}

exports.photoveiw = function(url, title, options){
    if( title == undefined ) {
      title = '';
    }

    if(typeof options == "undefined"){
        options = {};
    } 
    
    exec(function(){}, function(){}, "filepicker", "show", [url, title, options]);
}
