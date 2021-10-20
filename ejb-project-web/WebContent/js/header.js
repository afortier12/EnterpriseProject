/**
 * 
 */
 
const template = document.createElement('template');
template.innerHTML = `
 <div class="pure-menu pure-menu-horizontal">
 <a href="/ec-project-setup/index.html" class="pure-menu-heading pure-menu-link">Setup</a>
    <ul class="pure-menu-list">
        <li class="pure-menu-item">
            <a href="/ejb-project-web/index.jsp" class="pure-menu-link">Administration</a>
        </li>
        <li class="pure-menu-item">
            <a href="/ejb-project-web/LogoutServlet" class="pure-menu-link">Logout</a>
        </li>
 
    </ul>

</div>`

document.body.insertBefore(template.content, document.body.firstChild);

