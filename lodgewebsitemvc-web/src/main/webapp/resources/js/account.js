/* JavaScript for the account page */
var token = $("meta[name='_csrf']").attr("content");

$(function() {
    if (window.location.pathname === '/account') {
        if (window.location.hash === '#updated') {
            if (Modernizr.localstorage) {
                window.location.hash = '';
                var updateAlert = $("#update-alert");
                updateAlert.text(localStorage.getItem('successMessage'));
                updateAlert.slideDown();
                updateAlert.fadeTo(1500, 500).slideUp(500);
                localStorage.removeItem("successMessage");
            }
        }
    }
});

/* update user */
$('#save-email').click(function() {
    var user = {};
    user.email = $('#edit-email').val();
    user.confirmEmail = $('#confirm-edit-email').val();
    update('/account/email', JSON.stringify(user), $('#email-modal'), $('#email-modal .help-block ul'));
});

$('#save-first-name').click(function() {
    var user = {};
    user.firstName = $('#edit-first-name').val();
    update('/account/firstname', JSON.stringify(user), $('#first-name-modal'), $('#first-name-modal .help-block ul'));
});

$('#save-last-name').click(function() {
    var user = {};
    user.lastName = $('#edit-last-name').val();
    update('/account/lastname', JSON.stringify(user), $('#last-name-modal'), $('#last-name-modal .help-block ul'));
});

/* common functions */
function update(url, data, modal, errorList) {
    $.ajax({
        type: 'PUT',
        contentType : 'application/json',
        url: url,
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
            errorList.empty();
            $.each(data.responseJSON, function(index, value) {
                errorList.append('<li>' + value + '</li>');
            });
        }
    })
}