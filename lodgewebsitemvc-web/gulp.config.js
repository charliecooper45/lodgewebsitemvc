module.exports = function() {
    var config = {

        // js files to vet
        alljs: [
            // './src/**/*.js',
            './*.js',
            './src/main/webapp/resources/js/*.js',
            '!./src/main/webapp/resources/js/blueimp-gallery.min.js',
            '!./src/main/webapp/resources/js/bootstrap-image-gallery.min.js'
        ]
    };

    return config;
};
