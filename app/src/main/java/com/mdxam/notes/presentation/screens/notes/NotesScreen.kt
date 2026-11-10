package com.mdxam.notes.presentation.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mdxam.notes.R
import com.mdxam.notes.domain.Note
import com.mdxam.notes.presentation.ui.theme.OtherNotesColors
import com.mdxam.notes.presentation.ui.theme.PinnedNotesColors
import com.mdxam.notes.presentation.utils.DateFormatter

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel(),
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit
) {
    val state = viewModel.state.collectAsState()

    val currentState = state.value
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_note),
                        contentDescription = "add"
                        )
                }
            )
        }
    ){innerPadding ->
        //    val scrollState = remember { ScrollState(0) }
        LazyColumn(
            contentPadding = innerPadding
        ) {
            item {
                Title(text = "All Notes",
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp))
            }
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    query = currentState.query,
                    onQueryChange = {
                        viewModel.processCommand(NotesCommand.InputSearchQuery(it))
                    }
                )
            }
            item{
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                SubTitle(text = "Pinned",modifier = Modifier.padding(horizontal = 24.dp),)
            }
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    itemsIndexed(
                        items = currentState.pinnedNotes,
                        key = {_, note -> note.id}
                    ){index,note ->
                        NoteCard(
                            modifier = Modifier.widthIn(max = 160.dp),
                            note = note,
                            onNoteClick = onNoteClick,
                            onLongClick = {
                                viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                            },
                            backGroundColor = PinnedNotesColors[index % PinnedNotesColors.size]
                        )
                    }
                }
            }
            item{
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                SubTitle(text = "Others",modifier = Modifier.padding(horizontal = 24.dp))
            }
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }
            itemsIndexed(
                items = currentState.otherNotes,
                key = {_, note -> note.id}
            ){index,note ->
                NoteCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    note = note,
                    onNoteClick = onNoteClick,
                    onLongClick = {
                        viewModel.processCommand(NotesCommand.SwitchPinnedStatus(it.id))
                    },
                    backGroundColor = OtherNotesColors[index % OtherNotesColors.size]
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
){
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange : (String) -> Unit
){
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(10.dp)
            ),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search...",
                tint = MaterialTheme.colorScheme.onSurface
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun SubTitle(
    modifier: Modifier = Modifier,
    text: String
){
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    backGroundColor: Color,
    note: Note,
    onNoteClick: (Note) -> Unit,
    onLongClick : (Note) -> Unit
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backGroundColor)
            .combinedClickable(
                onClick = {onNoteClick(note)},
                onLongClick = {onLongClick(note)},
            )
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            fontSize = 14.sp,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = DateFormatter.formatDateToString(note.updatedAt),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = note.content,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 3,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis
        )
    }
}