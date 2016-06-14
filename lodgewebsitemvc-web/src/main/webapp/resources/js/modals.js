/* JavaScript for modals */
var token = $("meta[name='_csrf']").attr("content");

/* account */
$('#email-modal #save-email').click(function() {
    var user = {};
    user.email = $('#edit-email').val();
    user.confirmEmail = $('#confirm-edit-email').val();
    update('/account/email', JSON.stringify(user), $('#email-modal'), $('#email-modal .help-block ul'));
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
        success: function() {
            modal.modal('hide');
            location.reload(true);
        },
        error: function(data) {
            errorList.empty();
            $.each(data.responseJSON, function(index, value) {
                errorList.append('<li>' + value + '</li>');
            });
        }
    })
}