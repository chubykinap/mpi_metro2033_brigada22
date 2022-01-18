$(document).ready(function() {
    $(function(){
      var table = document.getElementsByTagName('table')[0],
      rows = table.getElementsByTagName('tr'),
      text = 'textContent' in document ? 'textContent' : 'innerText';

      for (var i = 1, len = rows.length; i < len; i++) {
        rows[i].children[0][text] = i + rows[i].children[0][text];
      }
    })
    $(document).on('click', '#delete', function(event) {
        event.preventDefault();
        var columns = $(this).closest('tr').find('td');
        console.log(columns);
        if (window.confirm("Удалить курьера?")){
            var courier_id = columns[0].innerHTML;
            console.log(courier_id);

            if (courier_id == '') {
              alert("Курьер не найден");
            }

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
});