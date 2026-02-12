# Legal AI Assistant

Legal AI Assistant is an AI-driven legal question answering and document generation system built using a Retrieval-Augmented Generation (RAG) based microservices architecture. The system integrates a Java Spring Boot application with a Python FastAPI AI service to provide context-aware legal responses based on BNSS, BNS, and BNA legal frameworks.

## Overview

This project enables users to:

- Submit legal queries through a secure web interface
- Receive AI-generated legal answers based on relevant legal provisions
- Generate legal documents automatically
- Store and retrieve chat history from a database

The application follows a microservices architecture where the frontend and AI services communicate through REST APIs.

## Architecture

User (Browser)  
→ Spring Boot Application (Frontend + Security)  
→ FastAPI AI Service (LangChain RAG Pipeline)  
→ Legal Knowledge Base (BNSS, BNS, BNA)  
→ MySQL Database (Chat and Response Storage)

---

## Tech Stack

### Backend (Java Service)
- Java
- Spring Boot
- Spring MVC
- Spring Security
- Thymeleaf
- REST APIs

### AI Service (Python Microservice)
- Python
- FastAPI
- LangChain
- Retrieval-Augmented Generation (RAG)

### Database
- MySQL

## How It Works

1. The user submits a legal query through the web interface.
2. Spring Boot validates the request and forwards it to the FastAPI service.
3. The LangChain-based RAG pipeline retrieves relevant legal sections.
4. The retrieved context is passed to the language model to generate a response.
5. The response is returned to Spring Boot.
6. Chat history and responses are stored in MySQL.



## Project Structure

