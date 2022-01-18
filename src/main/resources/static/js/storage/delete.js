$(document).ready(function() {
    $(document).on('click', '#delete', function(event) {
        event.preventDefault();
        var columns = $(this).closest('tr').find('td');
        if (window.confirm("Удалить предмет?")){
            var item_id = columns[0].innerHTML;
            $.ajax({
                type: "GET",
                url: "/storage/delete/" + item_id,
                success: function (data) {
                  console.log("success");
                  if (data.status == "Error"){
                    alert(data.data);
                    return;
                  }
                  console.log(data);
                  window.location.href = '/storage';
                }
            });
        }
    });
});