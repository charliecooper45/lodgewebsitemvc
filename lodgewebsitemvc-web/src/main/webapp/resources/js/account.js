/* JavaScript for the account page */
var token = $('meta[name="_csrf"]').attr('content');

$(function() {
    if (window.location.pathname.endsWith('/account')) {
        if (window.location.hash === '#updated') {
            if (Modernizr.localstorage) {
                window.location.hash = '';
                var updateAlert = $('#update-alert');
                updateAlert.text(localStorage.getItem('successMessage'));
                updateAlert.slideDown();
                updateAlert.fadeTo(1500, 500).slideUp(500);
                localStorage.removeItem('successMessage');
            }
        }
    }
});

$('#save-email').click(function() {
    var user = {};
    user.email = $('#edit-email').val();
    user.confirmEmail = $('#confirm-edit-email').val();
    ajax('PUT', 'account/email', JSON.stringify(user), $('#email-modal'));
});
setModalFocus($('#email-modal'), $('#edit-email'));

$('#save-password').click(function() {
    var user = {};
    user.password = $('#edit-password').val();
    user.confirmPassword = $('#confirm-edit-password').val();
    ajax('PUT', 'account/password', JSON.stringify(user), $('#password-modal'));
});
setModalFocus($('#password-modal'), $('#edit-password'));

$('#save-first-name').click(function() {
    var user = {};
    user.firstName = $('#edit-first-name').val();
    ajax('PUT', 'account/firstname', JSON.stringify(user), $('#first-name-modal'));
});
setModalFocus($('#first-name-modal'), $('#edit-first-name'));

$('#save-last-name').click(function() {
    var user = {};
    user.lastName = $('#edit-last-name').val();
    ajax('PUT', 'account/lastname', JSON.stringify(user), $('#last-name-modal'));
});
setModalFocus($('#last-name-modal'), $('#edit-last-name'));

/* reviews */
$('#save-review').click(function() {
    var review = {};
    review.review = $('#review-text').val();
    review.score = $('#review-score').val();
    ajax('POST', 'reviews', JSON.stringify(review), $('#add-review-modal'));
});
setModalFocus($('#add-review-modal'), $('#review-text'));

$('#delete-review-modal').on('show.bs.modal', function(e) {
    var reviewId = $(e.relatedTarget).data('review');
    $(e.currentTarget).find('#delete-review').attr('data-review', reviewId);
});
$('#delete-review').click(function() {
    ajax('DELETE', 'reviews/' + $(this).data('review'), null, $('#delete-review-modal'));
});

/* settings */
$('#language-select').on('change', function() {
    var modal = $('#language-modal');
    modal.find('#save-language').attr('data-language', $('option:selected', this).val());
    modal.modal('toggle');
});
$('#save-language').click(function() {
    ajax('PUT', 'account/language?language=' +
        $(this).data('language'), null, $('#language-modal'));
});

/* common functions */
function getBaseUrl() {
    return window.location.href.match(/^.*\//);
}

function setModalFocus(modal, element) {
    modal.on('shown.bs.modal', function () {
        element.focus();
    });
}

function ajax(method, url, data, modal) {
    $.ajax({
        type: method,
        contentType : 'application/json',
        url: getBaseUrl() + url,
        headers: {'X-CSRF-Token': token},
        dataType: 'json',
        data: data,
        success: function(data) {
            if (Modernizr.localstorage) {
                localStorage.setItem('successMessage', data[0]);
            }
            modal.modal('hide');
            window.location.hash = 'updated';
            window.location.reload(true);
        },
        error: function(data) {
            if (data.status === 403) {
                // user is not logged in
                window.location.replace(getBaseUrl() + '/login');
            } else {
                var errorList = modal.find('.help-block ul');
                errorList.empty();
                $.each(data.responseJSON, function(index, value) {
                    errorList.append('<li>' + value + '</li>');
                });
            }
        }
    });
}
