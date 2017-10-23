var get = function(configName, profile, site){
    var configPath = './' + profile + '/' + site + '/';

    try {
        return require(configPath + configName);
    } catch(e) {
        return null;
    }
};

module.exports.get = get;