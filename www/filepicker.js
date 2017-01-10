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

exports.chooespicture = function(message, win, fail){
    exec(win, fail, "filepicker", "chooespicture", [message]);
}

exports.chooesfiile = function(message, win, fail){
    exec(win, fail, "filepicker", "chooesfiile", [message]);
}
