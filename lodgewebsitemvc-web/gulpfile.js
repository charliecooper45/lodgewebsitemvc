var gulp = require('gulp');
var config = require('./gulp.config')();
var del = require('del');
var $ = require('gulp-load-plugins')({lazy: true});

gulp.task('clean', function() {
    log('Cleaning source folder');

    return clean(config.bowerComponents);
});

gulp.task('bower', ['clean'], function() {
    log('Running bower install');

    return $.bower('./bower_components');
});

gulp.task('vet', ['bower'], function() {
    log('Analyzing source with JSHint and JSCS');

    return gulp
        .src(config.allJs)
        .pipe($.print())
        .pipe($.jscs({configPath: '.jscsrc'}))
        .pipe($.jscs.reporter())
        .pipe($.jshint())
        .pipe($.jshint.reporter('jshint-stylish', {verbose: true}))
        .pipe($.jshint.reporter('fail'));
});

gulp.task('copy-resources', ['vet'], function() {
    log('Copying frontend resources from Bower to webapp');

    return gulp
        .src(config.bowerComponents)
        .pipe($.print())
        .pipe(gulp.dest(config.webappComponents));
});

gulp.task('default', ['copy-resources'], function() {
    log('Build completed successfully!');
});

//////////

function clean(path) {
    log('Cleaning: ' + $.util.colors.blue(path));

    return del(path);
}

function log(msg) {
    if (typeof(msg) === 'object') {
        for (var item in msg) {
            if (msg.hasOwnProperty(item)) {
                $.util.log($.util.colors.blue(msg[item]));
            }
        }
    } else {
        $.util.log($.util.colors.blue(msg));
    }
}
