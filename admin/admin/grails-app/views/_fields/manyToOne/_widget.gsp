<g:select name="${property}.id"
          from="${bean.getClass().list()}"
          value="${value?.id}"
          optionKey="id"
          noSelection="${['': '']}"
          class="form-control select2"/>