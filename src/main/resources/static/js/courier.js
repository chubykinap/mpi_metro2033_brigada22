$(document).ready(function() {
        $(function(){
          var table = document.getElementsByTagName('table')[0],
          rows = table.getElementsByTagName('tr'),
          text = 'textContent' in document ? 'textContent' : 'innerText';

          for (var i = 1, len = rows.length; i < len; i++) {
            rows[i].children[0][text] = i + rows[i].children[0][text];
          }
        })
        $(function(){
            $(document).on('click', '$delete', function(event)
            {
                event.preventDefault();
                var tbl_row = $(this).closest('tr');
                var courier_id = $(tbl_row).find("#courier_id").text();

                if (courier_id == '') {
                  alert("Курьер не найден");
                }

                var courier = {
                    "courier_id":courier_id
                };

                $.ajax({
                    type: "POST",
                    url: "/courier/delete",
                    contentType: "application/json",
                    data: JSON.stringify(courier),
                    success: function (data) {
                      console.log("success");
                      console.log(data);
                      window.location.href = data;
                    }
                });
             });
        })
    });