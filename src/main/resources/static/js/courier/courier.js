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
            $("#delete").on('click', function(event)
            {
                if (window.confirm("Удалить курьера?")){
                    event.preventDefault();
                    var columns = $(this).closest('tr').find('td');
                    console.log(columns);
                    var courier_id = columns[0].innerHTML;
                    console.log(courier_id);

                    if (courier_id == '') {
                      alert("Курьер не найден");
                    }

                    var courier = {
                        "courier_id":courier_id
                    };
                    $.ajax({
                        type: "GET",
                        url: "/courier/delete/" + courier_id,
                        success: function (data) {
                          console.log("success");
                          console.log(data);
                          window.location.href = '/courier';
                        }
                    });
                }
             });
        })
    });