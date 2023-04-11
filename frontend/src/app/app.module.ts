import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavComponent } from './main/nav/nav.component';
import { SidebarComponent } from './main/sidebar/sidebar.component';
import { AddBookmarkFormComponent } from './bookmark/add-bookmark-form/add-bookmark-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BookmarkListComponent } from './bookmark/bookmark-list/bookmark-list.component';
import { BookmarkComponent } from './bookmark/bookmark/bookmark.component';
import { LoginComponent } from './auth/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    SidebarComponent,
    AddBookmarkFormComponent,
    BookmarkListComponent,
    BookmarkComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
