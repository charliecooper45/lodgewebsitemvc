module.exports = function() {
    var config = {

        // js files to vet
        allJs: [
            './*.js',
            './src/main/webapp/resources/js/*.js',
            '!./src/main/webapp/resources/js/blueimp-gallery.min.js',
            '!./src/main/webapp/resources/js/bootstrap-image-gallery.min.js'
        ],
        bowerComponents: './bower_components/*/**',
        webappComponents: './src/main/webapp/resources/components'
    };

    return config;
};
