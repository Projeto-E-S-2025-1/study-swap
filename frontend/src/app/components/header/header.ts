import { Component } from '@angular/core';
import { Logout } from '../logout/logout';

@Component({
  selector: 'app-header',
  imports: [Logout],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {

}
